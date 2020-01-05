const userModel = require('../../models/user');
const contactModel = require('../../models/contact');
const imageModel = require('../../models/image');

// after login...
exports.addUser = (req, res) => {
    const { uid } = req.body;        // maybe facebook id
    const contact = new contactModel(); // contact data for user
    const image = new imageModel();     // image(gallery) data for user

    const user = new userModel();   // user to be added
    user.uid = uid;
    user.contact = contact;
    user.image = image;
    user.markModified('uid');
    user.markModified('contact');
    user.markModified('image');
    console.log(`user ${uid} in`)

    user.save((err) => {
        if (err) {
            console.error(err);
            res.json({ result: 0 })
        } else {
            res.json({ result: 1 })
        }
    })
};

// after withdrawal
exports.deleteUser = (req, res) => {
    res.send("TODO?? or not")
}