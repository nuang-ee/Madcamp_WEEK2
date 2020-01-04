const express = require('express');
const upload = require('./uploadImage.js');
// Init app
//const app = express();

// EJS
//app.set('view engine', 'ejs');

const router = express.Router()

// Public Folder
//app.use(express.static('./public'));

router.get('/', (req, res) => res.send("Welcome to Gallery Page!"));

//upload image(single)
router.post('/upload', (req, res) => {
  upload.saveImage(req, res)
});

//fetch image(single)
//router.get(

module.exports = router;
