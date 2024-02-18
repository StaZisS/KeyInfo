import('../../../../styles/peopleItem.css')

export const ButtonTeacher = ({callback}) => {
    return(
        <button onClick={callback} className="button-role student">
            <span className='buttonName'>Преподаватель</span>
        </button>
    )
}
