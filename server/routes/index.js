const express = require ('express');
const contact = require ('./contact');
const gallery = require ('./gallery/index');

const router = express.Router()

router.use('/contact', contact)
router.use('/gallery', gallery)
