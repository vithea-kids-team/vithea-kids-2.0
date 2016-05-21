'use strict';

describe('Controller: RegisterchildCtrl', function () {

  // load the controller's module
  beforeEach(module('clientApp'));

  var RegisterchildCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    RegisterchildCtrl = $controller('RegisterchildCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
