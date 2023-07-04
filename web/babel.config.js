const plugins = ["@vue/babel-plugin-transform-vue-jsx"];
if (process.env.VUE_APP_MODE === "production") {
  plugins.push("transform-remove-console")
}

module.exports = {
  plugins: plugins,
  presets: [
    '@vue/app'
  ]
}