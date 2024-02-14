import { KeysItem } from "../../keys/keyItem"

export const Keys = () => {
    return (
        <div className="col-10 col-md-8 mx-auto mt-3 bg-white p-4 shadow border-0">
            <h3>Список ключей:</h3>
            <KeysItem/>
            <KeysItem/>
            <KeysItem/>
        </div>
    )
}