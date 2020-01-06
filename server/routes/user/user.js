const userModel = require('../../models/user');
const contactModel = require('../../models/contact');
const imageModel = require('../../models/image');
const claimerModel = require('../../models/claimer');
const claimeeModel = require('../../models/claimee');

// after login...
exports.addUser = (req, res) => {
    const { uid } = req.body;        // maybe facebook id
    const contact = new contactModel(); // contact data for user
    const image = new imageModel();     // image data for user
    const claimee = new claimeeModel(); // claimee data for user
    const claimer = new claimerModel(); // claimer data for user

    const user = new userModel();   // user to be added
    user.uid = uid;
    user.contact = contact;
    user.image = image;
    user.claimee = claimee
    user.claimer = claimer
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
// check if user is in database
exports.checkUser = (req, res) => {
    const { uid } = req.body;
    userModel.find({ uid }, (err, [user]) => {  // [user], since find() returns list
        if (err) {
            console.error(err);
            res.status(500).send(e);
        } else {
            if (user) {
                res.json({ result: 1 }) // 1 if true
            } else {
                res.json({ result: 0 }) // 0 if there's no user in database
            }
        }
    });
}

// after withdrawal
exports.deleteUser = (req, res) => {
    res.send("TODO?? or not")
}