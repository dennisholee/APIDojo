//==============================================================================
// Cache content using ETag
//
// Example:
//   1. Get the ETag of the first request:
//      curl -s -I localhost:3000 | egrep -i etag | sed 's/.*:\s*\(.*\)/\1/'
//   2. Call again using the etag value:
//      curl -I -H 'If-None-Match: "${etag}"'  localhost:3000
//
//==============================================================================

const express = require('express')
const fresh   = require('fresh')

const PORT = process.env.PORT || 3000

const app = express()

// app.set('etag', 'strong')

app.get("/", (req, res) => {

  if (isFresh(req, res)) {
    res.statusCode = 304
    res.end()
    return
  }

  res.status(200).send("OK")
})



function isFresh (req, res) {
  return fresh(req.headers, {
    'etag': res.getHeader('ETag'),
    'last-modified': res.getHeader('Last-Modified')
  })
}

app.listen(PORT, () => console.log(`Listening on port ${PORT}`))


