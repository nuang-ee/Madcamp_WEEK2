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
            newImage.contentUrl = req.file.filename;
            //TODO : Check if this is right
            newImage.localCached = true;
            newImage.markModified("contentUrl")
            newImage.markModified("localCached")
            // append added image
            user.image.push(newImage);
            user.save(function(err) { 
              if (err) {
                res.json({ result: 0 })
              } else {
								res.json({ _id: newImage._id, result: 1 });
							}
            });
          }
        })
      }
    }
  });
};

exports.deleteImage = (req, res) => {
  const { uid, _id } = req.body;

  userModel.find({ uid }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send(e);
    } else {
      const image = user.image.find(e => JSON.stringify(e._id) === JSON.stringify(ObjectId(_id)))
      user.contact = user.image.filter(e => e !== image)
      user.save((err) => {
        if (err) {
          console.error(er)
          res.json({ result: 0 })
        } else {
          res.json({ result: 1 })
        }
      })
    }
  })
}

