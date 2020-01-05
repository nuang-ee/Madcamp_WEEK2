// Define Model
const imageModel = require('../../models/image');
const userModel = require('../../models/user');
const upload = require('../../lib/imageProcessor').upload;


exports.getImage = (req, res) => {
  const { uid } = req.body

  userModel.find({ uid }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send(e);
    } else {
      res.json(user.image);
    }
  })
}

exports.addImage = (req, res) => {
  upload(req, res, (err) => {
    if(err){
      console.log(err);
    } else {
      if(req.file == undefined){
        res.send('Error: No File Selected!');
      } else {
        const { uid } = req.body;
        userModel.find({ uid }, (err, [user]) => {
          if (err) {
            console.error(err);
            res.status(500).send(e)
          } else {
						const newImage = new imageModel()
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
        })
      }
    }
  });
};



