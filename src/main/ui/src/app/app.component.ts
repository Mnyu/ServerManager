import {Component, OnInit} from '@angular/core';
import {ServerService} from "./service/server.service";
import {BehaviorSubject, catchError, map, Observable, of, startWith} from "rxjs";
import {AppState} from "./interface/app-state";
import {CustomResponse} from "./interface/custom-response";
import {DataState} from "./enum/data-state.enum";
import {Status} from "./enum/status.enum";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  appState$: Observable<AppState<CustomResponse>>;
  readonly DataState = DataState;
  readonly Status = Status;

  private filterSubject = new BehaviorSubject<string>('');
  filterStatus$ = this.filterSubject.asObservable();

  private dataSubject = new BehaviorSubject<CustomResponse>(null);

  constructor(private serverService: ServerService) {}

  ngOnInit(): void {
    this.appState$ = this.serverService.servers$
      .pipe(
        map(response => {
          this.dataSubject.next(response);
          return { dataState: DataState.LOADED_STATE, appData: response}
        }),
        startWith({ dataState: DataState.LOADING_STATE }),
        catchError((errorMsg: string) => {
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
          return { dataState: DataState.LOADED_STATE, appData: this.dataSubject.value}
        }),
        startWith({ dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }),
        catchError((errorMsg: string) => {
          this.filterSubject.next('');
          return of({dataState: DataState.ERROR_STATE, error: errorMsg});
        })
      );
  }
}
