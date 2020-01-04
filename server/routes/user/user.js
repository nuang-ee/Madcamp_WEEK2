const userModel = require('../../models/user');
const contactModel = require('../../models/contact');
const imageModel = require('../../models/image');

// after login...
exports.addUser = (req, res) => {
    const { userId } = req.body;        // maybe facebook id
    const contact = new contactModel(); // contact data for user
    const image = new imageModel();     // image(gallery) data for user
/*

    required field 어떻게 할지 논의하기

    // init 'required' attributes
    contact.name = ""
    contact.phoneNumber = ""
    image.contentUrl = ""
*/
    const user = new userModel();   // user to be added
    user.facebook_id = userId;
    user.contact = contact;
    user.image = image;
    user.markModified('facebook_id');
    user.markModified('contact');
    user.markModified('image');

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