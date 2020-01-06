const contactModel = require("../../models/contact");
const userModel = require("../../models/user");
const upload = require("../../lib/imageProcessor").upload;
const ObjectId = require("mongodb").ObjectId;

exports.getContact = (req, res) => {
  const {
    uid
  } = req.body;

  // find user in user collection
  userModel.find({
      uid
    },
    (err, [user]) => {
      // [user], since find() returns list
      if (err) {
        console.error(err);
        res.status(500).send(e);
      } else {
        res.json(user.contact.filter(e => e.localCached === true)); // return uid's contact ('localCached === true' only)
      }
    }
  );
};
// add contact to user's data
exports.addContact = (req, res) => {
  upload(req, res, err => {
    if (err) {
      console.log(err);
    } else {
      const {
        uid,
        name,
        phoneNumber,
        email,
        localCached
      } = req.body;
      // find user
      userModel.find({
          uid
        },
        (err, [user]) => {
          // [user], since find() returns list
          if (err) {
            console.error(err);
            res.status(500).send(e);
          } else {
            // new contact
            const contact = new contactModel();
            contact.name = name || "-";
            contact.phoneNumber = phoneNumber || "-";
            contact.email = email || "-";
            contact.thumbnail = req.file.filename || ""; // image
            contact.localCached = localCached || "false";
            contact.markModified("name");
            contact.markModified("phoneNumber");
            contact.markModified("email");
            contact.markModified("thumbnail");
            contact.markModified("localCached");
            // append added contact
            user.contact.push(contact);
            user.save(err => {
              if (err) {
                console.error(err);
                res.json({
                  result: 0
                });
              } else {
                res.json({
                  _id: contact._id,
                  result: 1
                });
              }
            });
          }
        }
      );
    }
  });
};

exports.updateContact = (req, res) => {
  upload(req, res, err => {
    if (err) {
      console.log(err);
    } else {
      if (req.file == undefined) {
        res.send("Error: No File Selected!");
      } else {
        // uid is user id, _id is id of contact to be updated
        const {
          uid,
          _id,
          name,
          phoneNumber,
          email,
          localCached
        } = req.body;
        // find user in user collection
        userModel.find({
            uid
          },
          (err, [user]) => {
            // [user], since find() returns list
            if (err) {
              console.error(err);
              res.status(500).send(e);
            } else {
              let contact = user.contact.find(
                e => JSON.stringify(e._id) === JSON.stringify(ObjectId(_id))
              );
              if (contact) {
                const i = user.contact.indexOf(contact);
                contact.name = name || "-";
                contact.phoneNumber = phoneNumber || "-";
                contact.email = email || "-";
                contact.thumbnail = req.file.filename || ""; // image
                contact.localCached = localCached || "false";

                user.contact[i] = contact;
                user.save(err => {
                  if (err) {
                    console.error(err);
                    res.json({
                      result: 0
                    });
                  } else {
                    res.json({
                      result: 1
                    });
                  }
                });
              }
            }
          }
        );
      }
    }
  });
};

exports.deleteContact = (req, res) => {
  const {
    uid,
    _id
  } = req.body;

  // find user in user collection
  userModel.find({
      uid
    },
    (err, [user]) => {
      // [user], since find() returns list
      if (err) {
        console.error(err);
        res.status(500).send(e);
      } else {
        const contact = user.contact.find(
          e => JSON.stringify(e._id) === JSON.stringify(ObjectId(_id))
        );
        user.contact = user.contact.filter(e => e !== contact);
        user.save(err => {
          if (err) {
            console.error(err);
            res.json({
              result: 0
            });
          } else {
            res.json({
              result: 1
            });
          }
        });
      }
    }
  );
};