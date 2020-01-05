const express = require('express')
const contact = require('./contact')

const router = express.Router()

router.get('/', (req, res) => {
    res.send("Welcome to contact")
})
router.post('/get', contact.getContact);
router.post('/add', contact.addContact);
router.put('/update', contact.updateContact);
router.delete('/delete', contact.deleteContact);

module.exports = router;