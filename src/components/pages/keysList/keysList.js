import { KeysItem } from "./key/keyItem";


    // нужен запрос
    const keysData = [
        { keyNumber: 111, hasRequests: true, location: 'в деканате' },
        { keyNumber: 222, hasRequests: false, location: 'на руках y Rjuj-nj' },
        { keyNumber: 222, hasRequests: false, location: 'на руках y Rjuj-njsrtrtettr' },
        { keyNumber: 333, hasRequests: true, location: 'в деканате' }
    ];

export const Keys = () => {
    return (
        <div className="col-10 col-md-8 mx-auto mt-3 bg-white p-4 shadow border-0">
            <h3>Список ключей:</h3>
            {keysData.map((keyData, index) => (
                <KeysItem
                    key={index} 
                    keyNumber={keyData.keyNumber} 
                    hasRequests={keyData.hasRequests} 
                    location={keyData.location} 
                />
            ))}
        </div>
    )
}