const express = require('express');
const upload = require('./uploadImage.js');
// Init app
//const app = express();

// EJS
//app.set('view engine', 'ejs');
//
//for debug===================
/*
const path = require('path');
const multer = require('multer');
// Set The Storage Engine
const storage = multer.diskStorage({
  destination: './public/uploads/',
  filename: function(req, file, cb){
    cb(null,file.fieldname + '-' + Date.now() + path.extname(file.originalname));
  }
});

// Init Upload
const upload = multer({
  storage: storage,
  limits:{fileSize: 5 * 1024 * 1024},
  fileFilter: function(req, file, cb){
    checkFileType(file, cb);
  }
}).single('myImage');

// Check File Type
function checkFileType(file, cb){
  // Allowed ext
  const filetypes = /jpeg|jpg|png|gif/;
  // Check ext
  const extname = filetypes.test(path.extname(file.originalname).toLowerCase());
  // Check mime
  const mimetype = filetypes.test(file.mimetype);

  if(mimetype && extname){
    return cb(null,true);
  } else {
    cb('Error: Images Only!');
  }
}
*/
//============================


const router = express.Router()

// Public Folder
//app.use(express.static('./public'));

router.get('/', (req, res) => res.send("Welcome to Gallery Page!"));
router.post('/upload', (req, res) => {
  console.log(req)
  upload.getImage(req, res)
});
/*
router.post('/upload', (req, res) => {
  console.log(req)
  upload(req, res, (err) => {
    if (err) {
      console.log(err);
    } else {
      if(req.file == undefined) {
        res.send("err: no file selected");
      } else {
        res.send('file uploaded');
      }
    }
  });
});
*/
module.exports = router;
