module.exports = function ( grunt ) {

    /**
     * Load required Grunt tasks. These are installed based on the versions listed
     * in `package.json` when you do `npm install` in this directory.
     */
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-conventional-changelog');
    grunt.loadNpmTasks('grunt-ngmin');
    grunt.loadNpmTasks('grunt-html2js');

    /**
     * Load in our build configuration file.
     */
    var userConfig = require( './build.config.js' );

    /**
     * This is the configuration object Grunt uses to give each plugin its
     * instructions.
     */
    var taskConfig = {

        /**
         * We read in our `package.json` file so we can access the package name and
         * version. It's already there, so we don't repeat ourselves here.
         */
        pkg: grunt.file.readJSON("package.json"),

        /**
         * The banner is the comment that is placed at the top of our compiled
         * source files. It is first processed as a Grunt template, where the `<%=`
         * pairs are evaluated based on this very configuration object.
         */
        meta: {
            banner:
                '/**\n' +
                    ' * <%= pkg.name %> - v<%= pkg.version %> - <%= grunt.template.today("yyyy-mm-dd") %>\n' +
                    ' * <%= pkg.homepage %>\n' +
                    ' *\n' +
                    ' * Copyright (c) <%= grunt.template.today("yyyy") %> <%= pkg.author %>\n' +
                    ' * Licensed <%= pkg.licenses.type %> <<%= pkg.licenses.url %>>\n' +
                    ' */\n'
        },

        /**
         * The directories to delete when `grunt clean` is executed.
         */
        clean: [
            '<%= temp_dir %>',
            '<%= dev_dir %>/index.html',
            '<%= dev_dir %>/app',
            '<%= dev_dir %>/vendor',
            '<%= prod_dir %>/index.html',
            '<%= prod_dir %>/assets'
        ],

        /**
         * The `copy` task just copies files from A to B. We use it here to copy
         * our project assets (images, fonts, etc.) and javascripts into
         * `dev_dir`, and then to copy the assets to `prod_dir`.
         */
        copy: {
            dev_app_assets: {
                files: [
                    {
                        src: [ '**' ],
                        dest: '<%= dev_dir %>/assets/',
                        cwd: '<%= src_dir %>/assets',
                        expand: true
                    }
                ]
            },
            dev_vendor_assets: {
                files: [
                    {
                        src: [ '<%= vendor_files.assets %>' ],
                        dest: '<%= dev_dir %>/assets/',
                        cwd: '.',
                        expand: true,
                        flatten: true
                    }
                ]
            },
            dev_appjs: {
                files: [
                    {
                        src: [ '<%= app_files.js %>' ],
                        dest: '<%= dev_dir %>/',
                        cwd: '<%= src_dir %>',
                        expand: true
                    }
                ]
            },
            dev_apptpl: {
                files: [
                    {
                        src: [ '<%= app_files.tpl %>' ],
                        dest: '<%= dev_dir %>/',
                        cwd: '<%= src_dir %>',
                        expand: true
                    }
                ]
            },
            dev_vendorjs: {
                files: [
                    {
                        src: [ '<%= vendor_files.js %>' ],
                        dest: '<%= dev_dir %>/',
                        cwd: '.',
                        expand: true
                    }
                ]
            },
            dev_vendorcss: {
                files: [
                    {
                        src: [ '<%= vendor_files.css %>' ],
                        dest: '<%= dev_dir %>/',
                        cwd: '.',
                        expand: true
                    }
                ]
            },
            prod_assets: {
                files: [
                    {
                        cwd: '<%= src_dir %>/assets',
                        src: [ '**' ],
                        dest: '<%= prod_dir %>/assets',
                        expand: true
                    }
                ]
            }
        },

        /**
         * `grunt concat` concatenates multiple source files into a single file.
         */
        concat: {
            /**
             * The `prod_js` target is the concatenation of our application source
             * code and all specified vendor source code into a single file.
             */
            prod_js: {
                options: {
                    banner: '<%= meta.banner %>'
                },
                src: [
                    '<%= vendor_files.js %>',
                    'module.prefix',
                    '<%= temp_dir %>/**/*.js',
                    // TODO '<%= html2js.app.dest %>',
                    // TODO '<%= html2js.common.dest %>',
                    'module.suffix'
                ],
                dest: '<%= prod_dir %>/assets/<%= pkg.name %>-<%= pkg.version %>.js'
            }
        },

        less: {
            dev: {
                options: {
                    paths: ['<%= src_dir %>/<%= dev_dir %>/app/css']
                },
                files: [{
                    '<%= dev_dir %>/app/css/main.css': '<%= src_dir %>/<%= app_files.less %>'
                }]
            },
            prod: {
                options: {
                    paths: ['<%= src_dir %>/<%= dev_dir %>/app/css'],
                    compress: true
                },
                files: [{
                    '<%= prod_dir %>/app/css/main.css': '<%= src_dir %>/<%= app_files.less %>'
                }]
            }
        },

        /**
         * `jshint` defines the rules of our linter as well as which files we
         * should check. This file, all javascript sources, and all our unit tests
         * are linted based on the policies listed in `options`. But we can also
         * specify exclusionary patterns by prefixing them with an exclamation
         * point (!); this is useful when code comes from a third party but is
         * nonetheless inside `src/`.
         */
        jshint: {
            src: [
                '<%= src_dir %>/<%= app_files.js %>'
            ],/*
            test: [
                '<%= app_files.jsunit %>'
            ],*/
            gruntfile: [
                'Gruntfile.js'
            ],
            options: {
                curly: true,
                immed: true,
                newcap: true,
                noarg: true,
                sub: true,
                boss: true,
                eqnull: true
            },
            globals: {}
        },

        /**
         * HTML2JS is a Grunt plugin that takes all of your template files and
         * places them into JavaScript files as strings that are added to
         * AngularJS's template cache. This means that the templates too become
         * part of the initial payload as one JavaScript file. Neat!
         */
        html2js: {
            dev: {
                options: {
                    base: '<%= src_dir %>',
                    module: 'iteach.templates'
                },
                src: [ '<%= src_dir %>/<%= app_files.tpl %>' ],
                dest: '<%= dev_dir %>/app/iteach.templates.js'
            },
            prod: {
                options: {
                    base: '<%= src_dir %>',
                    module: 'iteach.templates'
                },
                src: [ '<%= src_dir %>/<%= app_files.tpl %>' ],
                dest: '<%= temp_dir %>/iteach.templates.js'
            }
        },

        /**
         * `ng-min` annotates the sources before minifying. That is, it allows us
         * to code without the array syntax.
         */
        ngmin: {
            prod: {
                files: [
                    {
                        src: [ '<%= app_files.js %>' ],
                        cwd: '<%= src_dir %>',
                        dest: '<%= temp_dir %>',
                        expand: true
                    }
                ]
            }
        },

        /**
         * Minify the sources!
         */
        uglify: {
            prod: {
                options: {
                    banner: '<%= meta.banner %>'
                },
                files: {
                    '<%= concat.prod_js.dest %>': '<%= concat.prod_js.dest %>'
                }
            }
        },

        /**
         * The `index` task compiles the `index.html` file as a Grunt template. CSS
         * and JS files co-exist here but they get split apart later.
         */
        index: {

            /**
             * During development, we don't want to have wait for compilation,
             * concatenation, minification, etc. So to avoid these steps, we simply
             * add all script files directly to the `<head>` of `index.html`. The
             * `src` property contains the list of included files.
             */
            dev: {
                dir: '<%= dev_dir %>',
                src: [
                    '<%= vendor_files.js %>',
                    '<%= dev_dir %>/app/**/*.js',
                    '<%= vendor_files.css %>',
                    '<%= dev_dir %>/app/css/**/*.css'
                ]
            },

            /**
             * When it is time to have a completely compiled application, we can
             * alter the above to include only a single JavaScript and a single CSS
             * file. Now we're back!
             */
            prod: {
                dir: '<%= prod_dir %>',
                src: [
                    '<%= concat.prod_js.dest %>',
                    '<%= vendor_files.css %>',
                    '<%= prod_dir %>/app/css/**/*.css'
                ]
            }
        },

        /**
         * And for rapid development, we have a watch set up that checks to see if
         * any of the files listed below change, and then to execute the listed
         * tasks when they do. This just saves us from having to type "grunt" into
         * the command-line every time we want to see what we're working on; we can
         * instead just leave "grunt watch" running in a background terminal. Set it
         * and forget it, as Ron Popeil used to tell us.
         *
         * But we don't need the same thing to happen for all the files.
         */
        delta: {
            /**
             * By default, we want the Live Reload to work for all tasks; this is
             * overridden in some tasks (like this file) where browser resources are
             * unaffected. It runs by default on port 35729, which your browser
             * plugin should auto-detect.
             */
            options: {
                livereload: true
            },

            /**
             * When the Gruntfile changes, we just want to lint it. In fact, when
             * your Gruntfile changes, it will automatically be reloaded!
             */
            gruntfile: {
                files: 'Gruntfile.js',
                tasks: [ 'jshint:gruntfile' ],
                options: {
                    livereload: false
                }
            },

            /**
             * When our JavaScript source files change, we want to run lint them and
             * run our unit tests.
             */
            jssrc: {
                files: [
                    '<%= src_dir %>/**/*.js '
                ],
                tasks: [ 'jshint:src', 'copy:dev_appjs' ]
            },

            /**
             * When assets are changed, copy them. Note that this will *not* copy new
             * files, so this is probably not very useful.
             */
            assets: {
                files: [
                    'src/assets/**/*'
                ],
                tasks: [ 'copy:dev_app_assets' ]
            },

            /**
             * When index.html changes, we need to compile it.
             */
            html: {
                files: [ '<%= src_dir %>/<%= app_files.html %>' ],
                tasks: [ 'index:dev' ]
            },

            /**
             * When our templates change, we only rewrite the template cache.
             */
            tpls: {
                files: [
                    '<%= src_dir %>/<%= app_files.tpl %>'
                ],
                tasks: [ 'copy:dev_apptpl', 'html2js:dev' ]
            },

            /**
             * When the CSS files change, we need to compile and minify them.
             */
            less: {
                files: [ 'src/**/*.less' ],
                tasks: [ 'less:dev' ]
            }
        }

    };

    grunt.initConfig( grunt.util._.extend( taskConfig, userConfig ) );

    /**
     * In order to make it safe to just compile or copy *only* what was changed,
     * we need to ensure we are starting from a clean, fresh build. So we rename
     * the `watch` task to `delta` (that's why the configuration var above is
     * `delta`) and then add a new task called `watch` that does a clean build
     * before watching for changes.
     */
    grunt.renameTask( 'watch', 'delta' );
    grunt.registerTask( 'watch', [ 'dev', 'delta' ] );

    /**
     * The default task is to dev and prod.
     */
    grunt.registerTask( 'default', [ 'dev', 'prod' ] );

    /**
     * The `dev` task gets your app ready to run for development and testing.
     */
    grunt.registerTask( 'dev', [
        'clean',
        'jshint',
        'less:dev',
        'copy:dev_app_assets',
        'copy:dev_vendor_assets',
        'copy:dev_appjs',
        'copy:dev_apptpl',
        'copy:dev_vendorjs',
        'copy:dev_vendorcss',
        'html2js:dev',
        'index:dev'
    ]);

    /**
     * The `prod` task gets your app ready for deployment by concatenating and
     * minifying your code.
     */
    grunt.registerTask( 'prod', [
        'clean',
        'less:prod',
        'copy:prod_assets',
        'html2js:prod',
        'ngmin:prod',
        'concat:prod_js',
        'uglify:prod',
        'index:prod'
    ]);

    /**
     * A utility function to get all app JavaScript sources.
     */
    function filterForJS ( files ) {
        return files.filter( function ( file ) {
            return file.match( /\.js$/ );
        });
    }

    /**
     * A utility function to get all app CSS sources.
     */
    function filterForCSS ( files ) {
        return files.filter( function ( file ) {
            return file.match( /\.css$/ );
        });
    }

    /**
     * The index.html template includes the stylesheet and javascript sources
     * based on dynamic names calculated in this Gruntfile. This task assembles
     * the list into variables for the template to use and then runs the
     * compilation.
     */
    grunt.registerMultiTask( 'index', 'Process index.html template', function () {
        var dirRE = new RegExp( '^('+grunt.config('dev_dir')+'|'+grunt.config('prod_dir')+')\/', 'g' );
        var jsFiles = filterForJS( this.filesSrc ).map( function ( file ) {
            return file.replace( dirRE, '' );
        });
        var cssFiles = filterForCSS( this.filesSrc ).map( function ( file ) {
            return file.replace( dirRE, '' );
        });

        grunt.file.copy(grunt.config('src_dir')+'/index.html', this.data.dir + '/index.html', {
            process: function ( contents, path ) {
                return grunt.template.process( contents, {
                    data: {
                        scripts: jsFiles,
                        styles: cssFiles,
                        version: grunt.config( 'pkg.version' )
                    }
                });
            }
        });
    });
};