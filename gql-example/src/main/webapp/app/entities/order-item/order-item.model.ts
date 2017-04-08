export class OrderItem {
    constructor(
        public id?: number,
        public quantity?: number,
        public value?: number,
        public productId?: number,
        public customerOrderId?: number,
    ) {
    }
}
