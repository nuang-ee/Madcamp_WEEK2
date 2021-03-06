// Define Model
const imageModel = require('../../models/image');
const userModel = require('../../models/user');
const upload = require('../../lib/imageProcessor').upload;
const ObjectId = require('mongodb').ObjectId;


exports.getImage = (req, res) => {
  const {
    uid
  } = req.body

  userModel.find({
    uid
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send({
        message: "Something wrong"
      });
    } else {
      if (user) {
        const imgList = user.image.filter(e => e.localCached)
          .map(e => {
            if (e.localCached) {
              e.contentUrl = "/static/" + e.contentUrl
              return e
            }
          })
        res.json(imgList) // send image path
      }
    }
  })
}
/**
 * 이미지 띄우기 참고
 * https://expressjs.com/ko/starter/static-files.html
 */
exports.addImage = (req, res) => {
  upload(req, res, (err) => {
    if (err) {
      console.error(err);
      res.status(500).send({
        message: "Something wrong"
      });
    } else {
      if (req.file == undefined) {
        res.send('Error: No File Selected!');
      } else {
        const {
          uid
        } = req.body;
        userModel.find({
          uid
        }, (err, [user]) => {
          if (err) {
            console.error(err);
            res.status(500).send({
              message: "Something wrong"
            });
          } else {
            if (user) {
              const newImage = new imageModel()
              newImage.contentUrl = req.file.filename;
              newImage.markModified("contentUrl")
              //TODO : Check if this is right
              newImage.localCached = true;
              newImage.markModified("localCached")
              // append added image
              user.image.push(newImage);
              user.save((err) => {
                if (err) {
                  console.error(err)
                  res.json({
                    result: 0
                  })
                } else {
                  res.json({
                    _id: newImage._id,
                    contentUrl: "/static/" + newImage.contentUrl,
                    result: 1
                  });
                }
              });
            } else {
              res.json({
                message: "user not found"
              })
            }
          }
        })
      }
    }
  });
};

exports.deleteImage = (req, res) => {
  const {
    uid,
    _id
  } = req.body;

  userModel.find({
    uid
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send({
        message: "Something wrong"
      });
    } else {
      if (user) {
        const image = user.image.find(e => JSON.stringify(e._id) === JSON.stringify(ObjectId(_id)))
        user.image = user.image.filter(e => e !== image)
        user.save((err) => {
          if (err) {
            console.error(err)
            res.json({
              result: 0
            })
          } else {
            res.json({
              result: 1
            })
          }
        })
      } else {
        res.json({
          message: "no user here"
        })
      }
    }
  })
}