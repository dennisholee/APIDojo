//==============================================================================
// API Versioning via the HTTP header ACCEPT
//
// Example:
//   curl localhost:3000/
//   curl -H 'Accept: application/json,application/vnd.forest.api+json;version=1' localhost:3000/
//   curl -H 'Accept: application/json,application/vnd.forest.api+json;version=2' localhost:3000/
//==============================================================================

const express  = require('express')
const v1Router = require('./v1_router.js')
const v2Router = require('./v2_router.js')

const PORT = process.env.PORT || 3000

const app = express()

/**
 * Middleware to extract version from HTTP header's ACCEPT parameter and add 
 * as prefix to URL path for internal routing.
 */
app.use((req, res, next) => {

  let acceptHeader = req.get("Accept")

  let headerTokens = acceptHeader
                        .split(',')
                        .map(i => i.trim())

  let version = "1"

  headerTokens.forEach(i => {
    if(i.startsWith('application/vnd.forest.api')) {

      let subtypeTokens = i.split(";").map(v => v.trim())

      subtypeTokens.forEach(j => {
        if(j.startsWith("version")) {
          let versionTokens = j.split("=").map(k => k.trim())
          if(versionTokens.length == 2) {
            version = versionTokens[1]
          }
        }
      })
    }
  })

  req.url = `/${version}` + req.url

  next()
})

app.use('/1', v1Router)

app.use('/2', v2Router)

app.get("/:version/*", (req, res) => {

  console.log(`Unknown version ${req.params.version}`)
  res.status(404).send(JSON.stringify({'Message' : 'Unknown API version.'}))
})

app.listen(PORT, () => console.log(`Listening on port ${PORT}`))
