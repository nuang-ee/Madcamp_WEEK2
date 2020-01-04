const express = require('express')
const contact = require('./contact')

const router = express.Router()

router.get('/:id', contact.getContact);

module.exports = router;