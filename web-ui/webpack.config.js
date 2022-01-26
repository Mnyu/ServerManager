const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require('copy-webpack-plugin');

const entry =
  process.env.NODE_ENV === 'production'
    ? ['babel-polyfill', './src/index']
    : [
      'babel-polyfill',
      'webpack-dev-server/client?http://0.0.0.0',
      'webpack/hot/only-dev-server', // "only" prevents reload on syntax errors
      './src/index',
    ];

const ejsStrings = {
  nodeEnvStr: '<%= NODE_ENV %>',
};

const envStrings = {
  nodeEnvStr: process.env.NODE_ENV,
}

module.exports = {
  mode: 'development',
  devtool: process.env.NODE_ENV === 'production' ? undefined : 'source-map',
  entry: {
    app: entry,
  },
  optimization: {
    splitChunks: {
      minSize: 5000000,
      cacheGroups: {
        // styles: {
        //   name: 'styles',
        //   test: /\.(sa|sc|c)ss$/,
        //   chunks: 'all',
        //   enforce: true,
        // },
      },
    },
  },
  output: {
    path: path.join(__dirname, 'dist'),
    filename:
      process.env.NODE_ENV === 'production'
        ? 'js/[name]_bundle.[chunkhash].js'
        : 'js/[name]_bundle.js',
    publicPath: '/',
    globalObject: 'this',
  },
  plugins: (process.env.NODE_ENV === 'production'
      ? [
        new HtmlWebpackPlugin({
          ...ejsStrings,
          inject: 'body',
          chunks: ['app', 'styles'],
          template: path.join(__dirname, 'src/index.html'),
          favicon: './src/favicon.ico'
        }),
      ]
      : [
        new HtmlWebpackPlugin({
          inject: 'body',
          ...envStrings,
          chunks: ['app', 'styles'],
          template: path.join(__dirname, 'src/index.html'),
        }),
        new webpack.HotModuleReplacementPlugin(),
      ]
  ).concat([
    new webpack.DefinePlugin({
      'process.env': {
        NODE_ENV: process.env.NODE_ENV
          ? JSON.stringify(process.env.NODE_ENV)
          : null,
      },
    }),
    // new CopyPlugin([{ from: 'assets', to: 'assets' }]),
  ]),
  resolve: {
    fallback: {
      fs: false
    },
    modules: ['node_modules'],
    alias: {
      'Components': path.resolve(__dirname, 'src/components/'),
      'Constants': path.resolve(__dirname, 'src/constants/'),
      'Containers': path.resolve(__dirname, 'src/containers/'),
      'Fonts': path.resolve(__dirname, 'src/fonts/'),
      'Helpers': path.resolve(__dirname, 'src/helpers/'),
      'Images': path.resolve(__dirname, 'src/images/'),
      'Svg': path.resolve(__dirname, 'src/svg/'),
      'Context': path.resolve(__dirname, 'src/context/'),
      'Pages': path.resolve(__dirname, 'src/pages/'),
      'Hooks': path.resolve(__dirname, 'src/hooks/'),
      'Hocs':  path.resolve(__dirname, 'src/hocs/'),
    }
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: ['babel-loader']
      },
      // {
      //   test: /\.js$/,
      //   loaders: ['babel-loader'],
      //   include: path.join(__dirname, 'src'),
      // },
      {
        test: /\.svg$/,
        use: [{
          loader: '@svgr/webpack',
          options: {
            svgoConfig: {
              plugins: {
                removeViewBox: false,
                collapseGroups: false,
                convertShapeToPath: false,
                removeUnknownsAndDefaults: false
              }
            }
          }
        }]
      },
      // {
      //   test: /\.(sa|sc|c)ss$/,
      //   use: [
      //     {
      //       loader: MiniCssExtractPlugin.loader,
      //       options: {
      //         publicPath: '/assets',
      //         hmr: process.env.NODE_ENV === 'development',
      //       },
      //     },
      //     'css-loader?localIdentName=[local]---[hash:base64:5]&minimize&-autoprefixer',
      //     'sass-loader',
      //   ],
      // },
      {
        test: /\.css$/,
        use: [
          {
            loader: 'style-loader',
          },
          {
            loader: 'css-loader',
          }
        ]
      },
      {
        test: /(\.scss$)/,
        use: [
          {
            loader: 'style-loader',
          },
          {
            loader: 'css-loader',
          },
          {
            loader: 'sass-loader',
            // options: {
            //   outputStyle: 'compressed',
            //   // includePaths: ['./node_modules'],
            // },
          },
        ],
      },
      {
        test: /\.ico$/i,
        //loader: 'file-loader?name=[name].[ext]',
        use: [
          {
            loader: 'file-loader',
            options: {
              name: "[name].[ext]"
            }
          }
        ]
      },
      {
        test: /\.(jpe?g|png|gif|woff|woff2|ttf)$/,
        use: [
          {
            loader: 'file-loader',
            options: {
              name: "[path][name].[ext]"
            }
          }
        ]
      },
    ],
  },
};
