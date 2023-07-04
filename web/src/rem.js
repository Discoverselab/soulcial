const baseSize = 192
function setRem() {
    let clientWidth = document.documentElement.clientWidth
    let scale
    if (clientWidth > 1280) {
        scale = document.documentElement.clientWidth / 1280
    }
    if (clientWidth <= 1280) {
        scale = document.documentElement.clientWidth / 1280
    }
    if (clientWidth <= 750) {
        scale = document.documentElement.clientWidth / 390
    }
    document.documentElement.style.fontSize = baseSize * scale + 'px'
}

setRem()
window.onresize = function (e) {
    setRem()
}