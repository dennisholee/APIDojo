const express = require('express')
const router  = express.Router()

router.get('/', (req, res) => {
  let payload = {'Message' : 'API VERSION ONE'}

  res.status(200).send(JSON.stringify(payload))
})

router.get('/health', (req, res) => {
  let payload = {'Message' : 'API VERSION ONE ALIVE'}

  res.status(200).send(JSON.stringify(payload))
})

module.exports = router
