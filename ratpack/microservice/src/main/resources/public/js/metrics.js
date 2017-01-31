/* eslint-env browser */
/* eslint-env jquery */

/**
 * Start the websocket
 */
$(document).ready(function () {
  if (!window.WebSocket) {
    alert('This dashboard will not work in your browser, a browser that supports websockets is required.')
  } else {
    console.log('Opening WebSocket')
    connectWs()
  }
})

/**
 * Add events to the websocket
 */
function connectWs () {
  if (!window.ws || window.ws.readyState !== WebSocket.OPEN) {
    window.ws = new WebSocket('ws://' + location.host + '/admin/metrics-report')

    window.ws.onopen = function (event) {
      console.log('WebSocket opened')
    }

    window.ws.onmessage = function (event) {
      console.log('WebSocket message received')
      updateData(event.data)
    }

    window.ws.onclose = function (event) {
      var timer = setTimeout(function () {
        console.log('Retrying connection...')
        connectWs()
        if (window.ws.readyState === WebSocket.OPEN) {
          clearTimeout(timer)
        }
      }, 1000)
    }
  }
}

/**
 * Update the metrics data
 */
function updateData (data) {
  $('#loading').hide()
  $('#json').html(JSON.stringify(JSON.parse(data), null, 2))
}
