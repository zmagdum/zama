export class Customer {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public phoneNumber?: string,
        public addressId?: number,
        public ordersId?: number,
    ) {
    }
}
