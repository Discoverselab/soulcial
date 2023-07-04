const plugins = ['@babel/plugin-proposal-optional-chaining',
  '@babel/plugin-proposal-nullish-coalescing-operator'];
// if (process.env.VUE_APP_MODE === "production") {
//   plugins.push("transform-remove-console")
// }

module.exports = {
  plugins: plugins,
  presets: [
    '@vue/app'
  ]
}