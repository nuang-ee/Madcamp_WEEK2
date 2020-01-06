const express = require('express');
const custom = require('./custom');

const router = express.Router()


router.get('/', (req, res) => res.send("Welcome to Custom Page!"));

router.post('/enroll');
router.post('/')


module.exports = router;
