// Define Model
const image = require('../../models/image');
// Create
const newImage = new image()
const upload = require('../../lib/imageProcessor').upload;

exports.saveImage = (req, res) => {
  console.log(req)
  upload(req, res, (err) => {
    if(err){
      console.log(err);
    } else {
      if(req.file == undefined){
        res.send('Error: No File Selected!');
      } else {
        console.log(`Uploaded file name : ${req.file.filename}`);
        newImage.contentUrl = req.file.filename;
        //TODO : Check if this is right
        newImage.localCached = true;
        newImage.markModified("contentUrl")
        newImage.markModified("localCached")
        newImage.save(function(err) { 
          if (err) {
            console.error(err);
            return;
          }
        });
        res.send('File Uploaded!');
      }
    }
  });
};




