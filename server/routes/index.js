const express = require ('express');
const home = require('./home');
const contact = require ('./contact');
const gallery = require ('./gallery/index');

const router = express.Router()

router.use('/', home)
router.use('/contact', contact)
router.use('/gallery', gallery)
