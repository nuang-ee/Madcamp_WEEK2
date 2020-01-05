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
                /**
                 * Todo: user_id (facebook id)로 구별하기
                 */
                const { userId, name, phoneNumber, email, thumbnail, localCached } = req.body;

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
                
                contact.save((err) => {
                    if (err) {
                        console.error(err);
                        res.json({ result: 0 });
                        return;
                    } else {
                        res.json({ result: 1 });
                    }
                })
            }
        }
    })
};

exports.updateContact = (req, res) => {

    const { _id } = req.params
    const { name, phoneNumber, email, thumbnail, localCached } = req.body;

    contactModel.findById(_id, (err, contact) => {
        // database error
        if (err) {
            return res.status(500).json({ error: 'database failure' });
        }
        // no contact in database
        if (!contact) {
            return res.status(404).json({ error: 'contact not found' });
        }

        if (name) contact.name = name
        if (phoneNumber) contact.phoneNumber = phoneNumber
        if (email) contact.email = email
        if (thumbnail) contact.thumbnail = thumbnail
        if (localCached) contact.localCached = localCached

        contact.save((err) => {
            // update fail
            if (err) {
                res.status(500).json({ error: 'failed to update' });
            } else {
                res.json({ message: 'contact updated' });
            }
        })
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