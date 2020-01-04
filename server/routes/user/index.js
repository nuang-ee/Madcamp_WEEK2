const express = require('express')
const user = require('./user')

const router = express.Router()

router.get('/', (req, res) => {
    res.send("Welcome to user")
})
router.post('/add', user.addUser);
router.post('/delete', user.deleteUser);    // To be implemented

module.exports = router;