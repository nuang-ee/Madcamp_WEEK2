const express = require('express')
const claimee = require('./claimee')

const router = express.Router()

router.get('/', (req, res) => {
  res.send('Welcome to claimee page')
})
router.post('/get', claimee.getClaimer)
router.put('/send', claimee.sendAmount)