const contactModel = require('../../models/contact');
const userModel = require('../../models/user');


exports.getContact = (req, res) => {
    const { userId } = req.params;

    try {
        const user = userModel.findById(userId, (err, results) => {
            if (err) {
                console.error(err);
                return;
            }
        });
        res.json(user.contact);
    } catch (e) {
        console.log(e);
        res.status(500).send(e);
    }

    // try {
    //     const contact = contactModel.findById(userId, (err, results) => {
    //         if (err) {
    //             console.error(err)
    //             return;
    //         }
    //     });
    //     res.json(contact)
    // } catch (e) {
    //     console.log(e);
    //     res.status(500).send(e);
    // }
};

exports.addContact = (req, res) => {
//    const { userId } = req.params;
    const { name, phoneNumber, email, thumbnail, localCached } = req.body;

    const contact = new contactModel()
    contact.name = name;
    contact.phoneNumber = phoneNumber;
    contact.email = email;
    contact.thumbnail = thumbnail;
    contact.localCached = localCached;

    contact.save((err) => {
        if (err) {
            console.error(err);
            res.json({ result: 0 });
            return;
        }

        res.json({ result: 1 });
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