const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    loginPage: path.resolve(__dirname, 'src', 'pages', 'loginPage.js'),
    signUpPage: path.resolve(__dirname, 'src', 'pages', 'signUpPage.js'),
    careerPage: path.resolve(__dirname, 'src', 'pages', 'careerPage.js'),
    dashboardPage: path.resolve(__dirname, 'src','pages','dashboardPage.js'),
    industryPage: path.resolve(__dirname, 'src', 'pages', 'industryPage.js'),

  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: false,
    port: 8080,
    open: "homePage.html",
    proxy: [
      {
        context: ['/Career','/Company','/Industry'],
        target: 'http://localhost:5001'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/homePage.html',
      filename: 'homePage.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/signUpForm.html',
      filename: 'signUpForm.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/careerPage.html',
      filename: 'careerPage.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/loginPage.html',
      filename: 'loginPage.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/dashBoard.html',
      filename: 'dashBoard.html',
      inject: false
    }),new HtmlWebpackPlugin({
      template: './src/industryPage.html',
      filename: 'industryPage.html',
      inject: false
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        }
      ]
    }),
    new CleanWebpackPlugin()
  ]
}
