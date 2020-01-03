const express = require('express')
const contact = require('./contact')

const router = express.Router()

router.get('/contact:id', contact.getContact);

module.exports = router;