export class CustomerOrder {
    constructor(
        public id?: number,
        public orderDate?: any,
        public comments?: string,
        public totalValue?: number,
        public salesPersonId?: number,
        public itemsId?: number,
        public customerId?: number,
    ) {
    }
}
