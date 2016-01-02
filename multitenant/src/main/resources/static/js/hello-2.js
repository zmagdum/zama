angular.module('hello', []).config(function($httpProvider) {

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

}).controller('navigation',

    function($scope, $http) {

        var authenticate = function(credentials, callback) {
            var headers = credentials ? {
                authorization : "Basic "
                + btoa(credentials.username + ":"
                    + credentials.password)
            } : {};

            $scope.user = ''
            $http.get('user', {
                headers : headers
            }).success(function(data) {
                if (data.name) {
                    console.log('user', data)
                    $scope.authenticated = true;
                    $scope.user = data.principal.firstName + ' ' + data.principal.lastName
                    $scope.admin = data && data.roles && data.roles.indexOf("ROLE_ADMIN")>0;
                } else {
                    $scope.authenticated = false;
                    $scope.admin = false;
                }
                callback && callback(true);
            }).error(function() {
                $scope.authenticated = false;
                callback && callback(false);
            });

        }

        authenticate();

        $scope.credentials = {};
        $scope.login = function() {
            authenticate($scope.credentials, function(authenticated) {
                $scope.authenticated = authenticated;
                $scope.error = !authenticated;
            })
        };

        $scope.logout = function() {
            $scope.products = [];
            $scope.user = "Anonymous";
            $http.post('logout', {}).success(function() {
                $scope.authenticated = false;
                $scope.admin = false;
            }).error(function(data) {
                console.log("Logout failed")
                $scope.authenticated = false;
                $scope.admin = false;
            });
        }

    }).controller('product', function($scope, $http) {
        $http.get('/products').success(function(data) {
            $scope.products = data;
        })
    });