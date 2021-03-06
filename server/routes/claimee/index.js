const express = require('express')
const claimee = require('./claimee')

const router = express.Router()

router.get('/', (req, res) => {
  res.send('Welcome to claimee page')
})
router.post('/get', claimee.getClaimee)
router.post('/add', claimee.addClaimee)
router.put('/received', claimee.receivedAmount)

module.exports = router