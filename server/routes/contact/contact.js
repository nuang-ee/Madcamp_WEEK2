const contactModel = require('../../models/contact');
const userModel = require('../../models/user');
const upload = require('../../lib/imageProcessor').upload;


exports.getContact = (req, res) => {
    const { uid } = req.body;

    // find user in user collection
    userModel.find({ uid }, (err, [user]) => {  // [user], since find() returns list
        if (err) {
            console.error(err);
            res.status(500).send(e);
        } else {
            res.json(user.contact)  // return uid's contact
        }
    });
};
// add contact to user's data
exports.addContact = (req, res) => {
    upload(req, res, (err) => {
        if (err) {
            console.log(err);
        } else {
            if (req.file == undefined) {
                res.send('Error: No File Selected!');
            } else {
                const { uid, name, phoneNumber, email, localCached } = req.body;
                // find user
                userModel.find({ uid }, (err, [user]) => {  // [user], since find() returns list
                    if (err) {
                        console.error(err);
                        res.status(500).send(e);
                    } else {
                        // new contact
                        const contact = new contactModel()
                        contact.name = name;
                        contact.phoneNumber = phoneNumber;
                        contact.email = email;
                        contact.thumbnail = req.file.filename;  // image
                        contact.localCached = localCached;
                        contact.markModified('name')
                        contact.markModified('phoneNumber')
                        contact.markModified('email')
                        contact.markModified('thumbnail')
                        contact.markModified('localCached')
                        // append added contact
                        user.contact.push(contact);
                        user.save((err) => {
                            if (err) {
                                console.error(err);
                                res.json({ result: 0 });
                            } else {
                                res.json({ result: 1 });
                            }
                        })
                    }
                });
            }
        }
    })
};

exports.updateContact = (req, res) => {

    // uid is user id, _id is id of contact to be updated
    const { uid, _id, name, phoneNumber, email, localCached } = req.body;
    console.log(req);

    upload(req, res, (err) => {
        if (err) {
            console.log(err);
        } else {
            if (req.file == undefined) {
                res.send('Error: No File Selected!');
            } else {
                // find user in user collection
                userModel.find({ uid }, (err, [user]) => {  // [user], since find() returns list
                    if (err) {
                        console.error(err);
                        res.status(500).send(e);
                    } else {
                        contactModel.findById(_id, (err, contact) => {
                            if (err) {
                                res.statue(500).json({ error: 'database failure' })
                            }
                            if (!contact) {
                                res.status(404).json({ error: 'contact not found' })
                            }
                            if (name) contact.name = name
                            if (phoneNumber) contact.phoneNumber = phoneNumber
                            if (email) contact.email = email
                            if (req.file.filename) contact.thumbnail = req.file.filename;
                            if (localCached) contact.localCached = localCached

                            user.contact.push(contact);
                            user.save((err) => {
                                if (err) {
                                    console.error(err);
                                    res.json({ result: 0 });
                                } else {
                                    res.json({ result: 1 });
                                }
                            })
                        })
                    }
                });
            }
        }
    })
}

exports.deleteContact = (req, res) => {
    const { _id } = req.params
    contactModel.remove({ _id: _id }, (err, output) => {
        if (err) {
            return res.statue(500).json({ error: 'database failure' });
        }
        res.status(204).end();  // No contents
    })
}
