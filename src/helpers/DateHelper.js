
export default class DateHelper{
    static normalizeDate(data){
        const date = new Date(data)

        const year = date.getUTCFullYear();
        const month = String(date.getUTCMonth() + 1).padStart(2, '0'); // добавляем 1, так как месяцы в JS нумеруются с 0
        const day = String(date.getUTCDate()).padStart(2, '0');
        const hours = String(date.getUTCHours()).padStart(2, '0');
        const minutes = String(date.getUTCMinutes()).padStart(2, '0');
        const seconds = String(date.getUTCSeconds()).padStart(2, '0');

        return {year, month, day, hours, minutes, seconds}
    }
}