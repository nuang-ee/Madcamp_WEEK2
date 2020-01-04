const express = require('express')
const contact = require('./contact')

const router = express.Router()

router.get('/', (req, res) => {
    res.send("Welcome to contact")
})
router.get('/:id', contact.getContact);
router.post('/add', contact.addContact); //router.post('/:id/add', contact.addContact);
router.post('/:id/update', contact.updateContact);
router.post('/:id/delete', contact.deleteContact);

module.exports = router;