const express = require('express')
const gallery = require('./gallery')

const router = express.Router()

router.get('/gallery/:id', gallery.getGallery);

module.exports = router;
