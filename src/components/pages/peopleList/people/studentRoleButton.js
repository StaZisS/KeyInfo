import('../../../../styles/peopleItem.css')

export const ButtonStudent = ({callback}) => {
    return(
        <button onClick={callback} className="button-role student">
            <span className='buttonName'>Студент</span>
        </button>
    )
}