angular.module('hello', ['ngRoute']).config(function ($routeProvider, $httpProvider) {
    $routeProvider.when('/ddd', {
        templateUrl: 'login.html',
        controller: 'navigation'
    }).when('/login', {
        templateUrl: 'login.html',
        controller: 'navigation'
    }).when('/upload', {
        templateUrl: 'upload.html',
        controller: 'upload'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

}).run(function ($rootScope, $location) {
    // register listener to watch route changes
    $rootScope.$on("$routeChangeStart", function (event, next, current) {
        console.log("running", $rootScope, $rootScope.authenticated)
        if (!$rootScope.authenticated) {
            // no logged user, we should be going to #login
            if (next.templateUrl == "/login.html") {
                // already going to #login, no redirect needed
            } else {
                // not going to #login, we should redirect now
                $location.path("/login");
            }
        } else {
            $location.path("/upload");
        }
    });
}).directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function () {
                scope.$apply(function () {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]).service('fileUpload', ['$http', function ( $http) {
    this.uploadFileToUrl = function (file, uploadUrl, doneCallback) {
        var fd = new FormData();
        fd.append('file', file);
        this.uploadResponse = $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
            .then(function (response) {
                console.log("upload success 1", response, response.data.message)
                doneCallback(response.data.message);
            })
            //.error(function (data) {
            //    console.log("upload error", data)
            //    return data.message;
            //});
    }
}]).controller(
    'navigation',
    function ($rootScope, $scope, $http, $location, $route) {

        $scope.tab = function (route) {
            return $route.current && route === $route.current.controller;
        };

        var authenticate = function (credentials, callback) {

            var headers = credentials ? {
                authorization: "Basic "
                + btoa(credentials.username + ":"
                    + credentials.password)
            } : {};

            $http.get('user', {
                headers: headers
            }).success(function (data) {
                if (data.name) {
                    $rootScope.authenticated = true;
                    $rootScope.userName = credentials ? credentials.username : "";
                } else {
                    $rootScope.authenticated = false;
                }
                callback && callback($rootScope.authenticated);
            }).error(function () {
                $rootScope.authenticated = false;
                callback && callback(false);
            });

        }

        authenticate();

        $scope.credentials = {};
        $scope.login = function () {
            authenticate($scope.credentials, function (authenticated) {
                $scope.authenticated = authenticated;
                $scope.error = !authenticated;
                if (authenticated) {
                    console.log("Login succeeded")
                    $location.path("/upload");
                    $scope.error = false;
                    $rootScope.authenticated = true;
//                    $scope.$apply( function () { $location.path("/upload") } );
                } else {
                    console.log("Login failed")
                    $location.path("/login");
                    $scope.error = true;
                    $rootScope.authenticated = false;
                }
            })
        };

        $scope.logout = function () {
            $http.post('logout', {}).success(function () {
                $rootScope.authenticated = false;
                $location.path("/");
            }).error(function (data) {
                console.log("Logout failed")
                $rootScope.authenticated = false;
                $location.path("/");
            });
        }

    }).controller('upload', ['$scope', 'fileUpload', function($scope, fileUpload) {
        $scope.uploadFile = function () {
            var file = $scope.fileToUpload;
            console.log('file is ');
            console.dir(file);
            var uploadUrl = "api/upload";
            $scope.uploadMessage = "";
            fileUpload.uploadFileToUrl(file, uploadUrl, function(data) {
                console.log("upload success", data)
                $scope.uploadMessage = data;
            });
        };
    }]);

