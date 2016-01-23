angular.module('hello', ['ngRoute', 'ngAnimate', 'ui.bootstrap']);
angular.module('hello').config(function($routeProvider, $httpProvider) {
    $routeProvider.when('/ddd', {
        templateUrl : 'login.html',
        controller : 'navigation'
    }).when('/login', {
        templateUrl : 'login.html',
        controller : 'navigation'
    }).when('/productlist', {
        templateUrl : 'productlist.html',
        controller : 'productlist'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

}).run( function($rootScope, $location) {
    // register listener to watch route changes
    $rootScope.$on( "$routeChangeStart", function(event, next, current) {
        console.log("running", $rootScope, $rootScope.authenticated)
        if (!$rootScope.authenticated) {
            // no logged user, we should be going to #login
            if ( next.templateUrl == "/login.html" ) {
                // already going to #login, no redirect needed
            } else {
                // not going to #login, we should redirect now
                $location.path( "/login" );
            }
        } else {
            $location.path( "/productlist" );
        }
    });
}).controller(
    'navigation',
    function($rootScope, $scope, $http, $location, $route) {

        $scope.tab = function(route) {
            return $route.current && route === $route.current.controller;
        };

        var authenticate = function(credentials, callback) {

            var headers = credentials ? {
                authorization : "Basic "
                + btoa(credentials.username + ":"
                    + credentials.password)
            } : {};

            $http.get('user', {
                headers : headers
            }).success(function(data) {
                if (data.name) {
                    $rootScope.authenticated = true;
                    $rootScope.userName = data.principal.firstName + ' ' + data.principal.lastName
                    $rootScope.user = data.principal.name;
                    $scope.admin = data && data.roles && data.roles.indexOf("ROLE_ADMIN")>0;
                } else {
                    $rootScope.authenticated = false;
                }
                callback && callback($rootScope.authenticated);
            }).error(function() {
                $rootScope.authenticated = false;
                callback && callback(false);
            });

        }

        authenticate();

        $scope.credentials = {};
        $scope.login = function() {
            authenticate($scope.credentials, function(authenticated) {
                $scope.authenticated = authenticated;
                $scope.error = !authenticated;
                if (authenticated) {
                    console.log("Login succeeded")
                    $location.path("/products");
                    $scope.error = false;
                    $rootScope.authenticated = true;
//                    $scope.$apply( function () { $location.path("/products") } );
                } else {
                    console.log("Login failed")
                    $location.path("/login");
                    $scope.error = true;
                    $rootScope.authenticated = false;
                }
            })
        };

        $scope.logout = function() {
            $http.post('logout', {}).success(function() {
                $rootScope.authenticated = false;
                $location.path("/");
            }).error(function(data) {
                console.log("Logout failed")
                $rootScope.authenticated = false;
                $location.path("/");
            });
        }

        $http.get('/users').success(function(data) {
            console.log('found users', data)
            $scope.users = data;
        })

    }).controller('productlist', function($rootScope, $scope, $uibModal, $http, $log) {
        $http.get('/companyByUserName/'+$scope.user).success(function(data) {
            $scope.company = data;
        })

        $http.get('/products').success(function(data) {
            // storing product in root scope so that we can rewrite it from productDialogCtrl
            // setup service so that we can communicate from two controllers
             $rootScope.products = data;
        })

        $scope.animationsEnabled = true;
        $scope.addProduct = function (size) {
            var modalInstance = $uibModal.open({
                animation: $scope.animationsEnabled,
                templateUrl: 'productdialog.html',
                controller: 'productDialogCtrl',
                size: size,
                resolve: {
                    products: function () {
                        return $scope.products;
                    },
                    company: function () {
                        return $scope.company;
                    }
                }
            });

            modalInstance.result.then(function (selectedItem) {
                $scope.product = selectedItem;
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };
    }).controller('productDialogCtrl', function($rootScope, $scope, $uibModalInstance, $http, products, company) {
        $scope.ok = function () {
            console.log("modal ok", $scope.product, company, $scope.user);
            $scope.product.companyId = company.companyKey;
            $http.post('/product', $scope.product).success(function(data, status, headers) {
                $http.get('/products').success(function(data) {
                    $rootScope.products = data;
                })
            });
            $uibModalInstance.close($scope.product);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    });
var checkRouting = function ($q, $rootScope, $location) {
    return $rootScope.authenticated
};