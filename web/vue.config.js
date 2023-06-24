const px2rem = require('postcss-px2rem')
const resolve = dir => path.join(__dirname, dir)
const postcss = px2rem({
    remUnit: 192
})
// const target = process.env.APP_API_URL;
const WebpackBundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
const compressionWebpackPlugin = require('compression-webpack-plugin'); 
const productionGzipExtensions = /\.(js|css|json|txt|html|ico|svg)(\?.*)?$/i;
const cdn = {
    externals: {
        // vue: 'Vue',
        // axios: 'axios',
        // 'element-ui': 'ElementUI'
    },
    css: [
        'https://unpkg.com/element-ui@2.15.1/lib/theme-chalk/index.css'
    ]
}

// SEO优化
const PrerenderSPAPlugin = require('prerender-spa-plugin')
const Renderer = PrerenderSPAPlugin.PuppeteerRenderer
const webpack = require('webpack')
const path = require('path')
// const TerserPlugin = require('terser-webpack-plugin')
module.exports = {
    publicPath: '/',
    outputDir: process.env.outputDir,
    assetsDir: 'static',
    lintOnSave: false,
    productionSourceMap: false,
    css: {
        loaderOptions: {
            postcss: {
                plugins: [postcss]
            }
        },
    },
    chainWebpack: (config) => {
        config.plugin('html').tap((args) => {
            args[0].cdn = cdn
            return args
        });
        config.module.rule("svg").exclude.add(resolve("src/icons")).end();
        config.module
          .rule("icons")
          .test(/\.svg$/)
          .include.add(resolve("src/icons"))
          .end()
          .use("svg-sprite-loader")
          .loader("svg-sprite-loader")
          .options({
            symbolId: "icon-[name]",
          })
          .end();
    },
    configureWebpack: {

        plugins: [
            // new PrerenderSPAPlugin({
            //     staticDir: path.join(__dirname, 'dist'),
            //     routes: ['/', '/buy', '/mechanics', '/faq', '/zoo', '/backpack', '/Leaderboard', '/nft_details', '/auction_details', '/rule', '/species', ],
            //     renderer: new Renderer({
            //         inject: { 
            //             foo: 'bar'
            //         },
            //         headless: false,
            //         renderAfterDocumentEvent: 'render-event' 
            //     })
            // }),
            new webpack.optimize.MinChunkSizePlugin({
                minChunkSize: 10000 
            }),
            new WebpackBundleAnalyzerPlugin(),
            new compressionWebpackPlugin({
                asset: '[path].gz[query]', 
                algorithm: 'gzip',
                test: productionGzipExtensions,
                threshold: 10240,
                minRatio: 0.8,
            })
        ],
        optimization: {
            runtimeChunk: 'single',
            splitChunks: {
                chunks: 'all',
                maxInitialRequests: Infinity,
                minSize: 20000,
                cacheGroups: {
                    vendor: {
                        test: /[\\/]node_modules[\\/]/,
                        name(module) {
                            // get the name. E.g. node_modules/packageName/not/this/part.js
                            // or node_modules/packageName
                            const packageName = module.context.match(/[\\/]node_modules[\\/](.*?)([\\/]|$)/)[1]
                            // npm package names are URL-safe, but some servers don't like @ symbols
                            return `npm.${packageName.replace('@', '')}`
                        }
                    }
                }
            }
        },
        externals: cdn,
        resolve: {
            extensions: [".ts", ".tsx", ".js", ".json"]
        },
        module: {
            rules: [{
                test: /\.tsx?$/,
                loader: 'ts-loader',
                exclude: /node_modules/,
                options: {
                    appendTsSuffixTo: [/\.vue$/],
                }
            }]
        },
    },
};