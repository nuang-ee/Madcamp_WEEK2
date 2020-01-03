const express = require ('express');
const home = require('./home');
const contact = require ('./contact');
// 여기에 gallery 추가하고 폴더 만들기 ㄱㄱ

const router = express.Router()

router.use('/', home)
router.use('/contact', contact)
// 여기도 갤러리 추가 해야댐