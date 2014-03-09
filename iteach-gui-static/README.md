iteach GUI
==========

This module contains the static files (CSS, JS, images...) for the iTeach GUI.

## Building the client side

```
sudo npm -g install grunt-cli karma bower
npm install
bower install
```

Then, to build in development mode (no compression):

```
grunt clean dev
```

To build in development mode and scan for changes:

```
grunt watch
```

To build for the production:

```
grunt clean prod
```


## Remaining actions

* TODO Version in `bower.json` and `package.json`.
* TODO iTeach logo
