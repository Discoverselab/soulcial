import copy from './copy'

// Custom instruction
const directives = {
  copy
}
// Batch registration instruction
export default {
  install(Vue) {
    Object.keys(directives).forEach(key => {
      Vue.directive(key, directives[key])
    })
  }
}