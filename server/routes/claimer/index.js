const express = require('express')
const claimer = require('./claimer')

const router = express.Router()

router.get('/', (req, res) => {
  res.send('Welcome to claimer page')
})
router.post('/get', claimer.getClaimer);
router.post('/add', claimer.addClaim)
router.put('/receive', claimer.receiveAmount);

module.exports = router