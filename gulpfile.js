'use strict';
var fs = require('fs');
function readFile(path) {
    var file = fs.readFileSync(path);
    return file ? JSON.parse(file) : false;
}

var pkg = readFile('./package.json'),
    gulp = require('gulp'),
    sass = require('gulp-sass'),
    mqpacker = require('css-mqpacker'),
    del = require('del'),
    plugins = require('gulp-load-plugins')({
        pattern: ['gulp-*', 'gulp.*'],
        replaceString: /\bgulp[\-.]/,
        camelize: true,
        rename: {
        },
        lazy: true
    }),
    args = plugins.util.env,
    info = pkg.info,
    resources = info.resources,
    paths = info.paths,
    config = {
        sassPattern: paths.scss + '**/*.scss',
        cssPattern: paths.css + '**/*.css',
        mapPattern: paths.css + '**/*.map',
        minPattern: paths.min + '*.css'
    },
    sassOptions = {
        errLogToConsole: true,
        outputStyle: 'expanded',
        sourceComments: 'map',
        includePaths: ['./node_modules']
    };

//Delete output directory
gulp.task('clean:css', function () {
    return del([
            resources + config.cssPattern,
            resources + config.minPattern
    ]);
});

//Separate task leveraging POST CSS functions (not necessary for styles)
// could be necessary for future JS
//gulp.task('processors', function () {
//    var processors = [
//        autoprefixer({browsers: ['last 5 versions', '> 5%', 'Firefox ESR', 'safari 5', 'ie 9', 'opera 12.1', 'ios 6', 'android 4']}),
//        mqpacker,
//        csswring
//    ]
//    return gulp.src(resources + config.cssPattern)
//        .pipe(postcss(processors))
//        .pipe(gulp.dest(resources + config.cssPattern));
//});

//Compile Sass
gulp.task('sass', function () {
    var processors = [mqpacker];

    return gulp.src(resources + config.sassPattern)
        .pipe(plugins.sourcemaps.init())
        .pipe(sass(sassOptions).on('error', sass.logError))
        .pipe(plugins.autoprefixer({browsers: ['last 5 versions', '> 5%', 'Firefox ESR']}))
        .pipe(plugins.postcss(processors))
        .pipe(plugins.sourcemaps.write())
        .pipe(gulp.dest(resources + paths.css));
});

//Watchers
gulp.task('watch', ['sass'], function () {
    return gulp
        //Watch the input folder and run a task when changes occur
        .watch(resources + config.sassPattern, ['sass', 'concat'])
        //When change occurs output a message
        .on('change', function (event) {
            console.log('File' + event.path + 'was' + event.type + ', file updated...');
        });
});

gulp.task('concat', ['sass'], function () {
    return gulp
        .src([
                resources + paths.css + 'base.css',
                resources + paths.css + 'layouts/*.css',
                resources + paths.css + 'sections/*.css',
                resources + paths.css + 'pages/*.css'
        ])
        .pipe(plugins.concatCss("famos-single.css"))
        .pipe(gulp.dest(resources + paths.css));
});

var defaultTasks = ['sass', 'concat', 'watch', 'concat'],
    cleanCss = ['clean:css'],
    productionTasks = ['sass', 'concat'];

//Clean
gulp.task('clean', cleanCss);

//Default tasks
gulp.task('default', defaultTasks);

//Production
gulp.task('build', productionTasks);