//==============================================================================
// Content negotiation via HTTP's Header "Accept"
//
// Sample cURL test commands:
//   curl -H localhost:3000
//   curl -H "content-type: application/json" localhost:3000
//   curl -H "content-type: application/xml" localhost:3000
//   curl -H "content-type: application/xml" -H "Accept: application/json" localhost:3000
//   curl -H "content-type: application/javascript" -H "Accept: application/json" localhost:3000
//==============================================================================
const express = require("express")
const xml2js  = require("xml2js")

const app = express()

app.get("/", (req, res) => {

    let acceptHeader = req.headers.accept;

    let acceptTokens = null

    //--------------------------------------------------------------------------
    // Extract client's acceptable list of mime type.
    //--------------------------------------------------------------------------
    if(acceptHeader) {
      acceptTokens = acceptHeader
                      .toLowerCase()
                      .split(',')
                      .map(i => {return i.indexOf(';') >= 0 ? i.substring(0, i.indexOf(';') - 1) : i})
                      .map(i => i.trim())
    } 

    //--------------------------------------------------------------------------
    // Assume JSON if client did not provide an acceptable list of mime type
    //--------------------------------------------------------------------------
    if(acceptTokens == null || 
        acceptTokens.length < 0 || 
        (acceptTokens.length == 1 && acceptTokens[0] == "*/*") ) {
      acceptTokens = ["application/json"]
    }

    let payload = {"key" : "value"} // Arbitrary response value.
    let responsePayload = null;
    let type = null;
    let status = 200

    if(acceptTokens.includes("application/json")) {
      responsePayload = JSON.stringify(payload)
      type = "application/json"
    } else if (acceptTokens.includes("application/xml")) {
      let builder = new xml2js.Builder()
      responsePayload = builder.buildObject(payload)
      type = "application/xml"
    } else {
      status = 406
      type = "application/json"

      responsePayload =
          JSON.stringify(
              {
                "Message" : "Not Acceptable. Support application/json or application/xml only.",
                "Accept Given"    : acceptTokens.join(",")
              }
          )
    }

    res.set({'Content-Type': type})
    res.status(status).send(responsePayload)
})

const PORT = process.env.PORT || 3000

app.listen(3000,  () => console.log(`Listening on port ${PORT}`))
