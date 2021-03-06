import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {ServerService} from "./service/server.service";
import {BehaviorSubject, catchError, map, Observable, of, startWith} from "rxjs";
import {AppState} from "./interface/app-state";
import {CustomResponse} from "./interface/custom-response";
import {DataState} from "./enum/data-state.enum";
import {Status} from "./enum/status.enum";
import {NgForm} from "@angular/forms";
import {Server} from "./interface/server";
import {NotificationService} from "./service/notification.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent implements OnInit {

  readonly imageUrl = window.location.protocol + '//' + window.location.host + '/api/server/image/server.png';

  appState$: Observable<AppState<CustomResponse>>;
  readonly DataState = DataState;
  readonly Status = Status;

  private filterSubject = new BehaviorSubject<string>('');
  filterStatus$ = this.filterSubject.asObservable();

  private isLoading = new BehaviorSubject<boolean>(false);
  isLoading$ = this.isLoading.asObservable();

  private dataSubject = new BehaviorSubject<CustomResponse>(null);

  constructor(private serverService: ServerService,
              private notifier: NotificationService) {}

  ngOnInit(): void {
    this.appState$ = this.serverService.servers$
      .pipe(
        map(response => {
          this.notifier.onDefault(response.message);
          this.dataSubject.next(response);
          return { dataState: DataState.LOADED_STATE, appData: response}
        }),
        startWith({ dataState: DataState.LOADING_STATE }),
        catchError((errorMsg: string) => {
          this.notifier.onError(errorMsg);
          return of({dataState: DataState.ERROR_STATE, error: errorMsg});
        })
      );
  }

  pingServer(ipAddress: string): void {
    this.filterSubject.next(ipAddress);
    this.appState$ = this.serverService.ping$(ipAddress)
      .pipe(
        map(response => {
          const index = this.dataSubject.value.data.servers.findIndex(server => server.id === response.data.server.id)
          this.dataSubject.value.data.servers[index] = response.data.server;
          this.filterSubject.next('');
          this.notifier.onDefault(response.message);
          return { dataState: DataState.LOADED_STATE, appData: this.dataSubject.value};
        }),
        startWith({ dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }),
        catchError((errorMsg: string) => {
          this.notifier.onError(errorMsg);
          this.filterSubject.next('');
          return of({dataState: DataState.ERROR_STATE, error: errorMsg});
        })
      );
  }

  filterServers(status: Status): void {
    this.appState$ = this.serverService.filter$(status, this.dataSubject.value)
      .pipe(
        map(response => {
          this.notifier.onDefault(response.message);
          return { dataState: DataState.LOADED_STATE, appData: response};
        }),
        startWith({ dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }),
        catchError((errorMsg: string) => {
          this.notifier.onError(errorMsg);
          return of({dataState: DataState.ERROR_STATE, error: errorMsg});
        })
      );
  }

  saveServer(serverForm: NgForm): void {
    this.isLoading.next(true);
    let newServer = <Server>serverForm.value;
    newServer.imageUrl = this.imageUrl;
    this.appState$ = this.serverService.save$(newServer)
      .pipe(
        map(response => {
          this.dataSubject.next({...response, data:{ servers: [...this.dataSubject.value.data.servers, response.data.server] }});
          this.notifier.onDefault(response.message);
          document.getElementById('closeModal').click();
          this.isLoading.next(false);
          serverForm.resetForm({ status : this.Status.SERVER_DOWN });
          return { dataState: DataState.LOADED_STATE, appData: this.dataSubject.value};
        }),
        startWith({ dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }),
        catchError((errorMsg: string) => {
          this.notifier.onError(errorMsg);
          this.isLoading.next(false);
          return of({dataState: DataState.ERROR_STATE, error: errorMsg});
        })
      );
  }

  deleteServer(server: Server): void {
    this.appState$ = this.serverService.delete$(server.id)
      .pipe(
        map(response => {
          this.dataSubject.next(
      { ...response,
              data: {servers: this.dataSubject.value.data.servers.filter(serv => serv.id !== server.id)}
            });
          this.notifier.onDefault(response.message);
          return { dataState: DataState.LOADED_STATE, appData: this.dataSubject.value};
        }),
        startWith({ dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }),
        catchError((errorMsg: string) => {
          this.notifier.onError(errorMsg);
          return of({dataState: DataState.ERROR_STATE, error: errorMsg});
        })
      );
  }

  printReport(): void {
    this.notifier.onDefault('Report downloaded');
    // window.print(); // For pdf
    let dataType = 'application/vnd.ms-excel.sheet.macroEnabled.12';
    let tableSelect = document.getElementById('servers');
    let tableHtml = tableSelect.outerHTML.replace(/ /g, '%20');
    let downloadLink = document.createElement('a');
    document.body.appendChild(downloadLink);
    downloadLink.href = 'data:' + dataType + ', ' + tableHtml;
    downloadLink.download = 'servers-report.xls';
    downloadLink.click();
    document.body.removeChild(downloadLink);
  }
}
