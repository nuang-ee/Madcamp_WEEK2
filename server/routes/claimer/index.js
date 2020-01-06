const express = require('express')
const claimer = require('./claimer')

const router = express.Router()

router.get('/', (req, res) => {
  res.send('Welcome to claimer page')
})
router.post('/get', claimer.getClaimee);
router.put('/receive', claimer.receiveAmount);