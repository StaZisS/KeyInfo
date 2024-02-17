import { PeopleItem } from "./people/peopleItem";


    // нужен запрос
    const peopleData = [
        { name: 'Иванов Юрий Петрович' },
        { name: 'Порпоппо ОПП ЛПНПнп' },
        { name: 'ПРПР рпПп РПНПНРПРО' },
        { name: 'врврвр вроврврв воврвр вовр' }
    ];

export const PeopleList = () => {
    return (
        <div className="col-10 col-md-8 mx-auto mt-3 bg-white p-4 shadow border-0">
        <h3>Пользователи:</h3>
            {peopleData.map((Data, index) => (
                <PeopleItem
                    key={index} 
                    name={Data.name} 
                />
            ))}
        </div>
    )
}