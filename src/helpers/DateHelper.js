
export default class DateHelper{
    static normalizeDate(data){
        const date = new Date(data)

        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // добавляем 1, так как месяцы в JS нумеруются с 0
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');

        return {year, month, day, hours, minutes, seconds}
    }
}